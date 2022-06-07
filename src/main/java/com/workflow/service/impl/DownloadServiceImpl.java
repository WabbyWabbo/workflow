package com.workflow.service.impl;

import com.workflow.model.params.PageParam;
import com.workflow.pojo.Download;
import com.workflow.pojo.json.All;
import com.workflow.service.DownloadService;
import com.workflow.util.FastJsonUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import vo.DirResult;
import vo.PageResult;
import vo.Result;

import java.util.ArrayList;
import java.util.List;

@Service
public class DownloadServiceImpl implements DownloadService {

    @Override
    public Result listDownload(PageParam pageParam) {
        Integer currentPage = pageParam.getCurrentPage();
        Integer pageSize = pageParam.getPageSize();

        All all = FastJsonUtils.readFile("data.json");
        List<Download> list = all.getDownload().getList();
        PageResult<Download> pageResult = new PageResult<>();

        if (list.size() == 0) {
            pageResult.setList(null);
        }

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = startIndex + pageSize - 1;
        List<Download> temp = new ArrayList<>();
        for (int i = startIndex; i < list.size() && i <= endIndex; i++) {
            temp.add(list.get(i));
        }
        pageResult.setList(temp);
        pageResult.setTotal(all.getDownload().getTotal());
        return Result.success(pageResult);

    }

    @SneakyThrows
    @Override
    public Result add(Download download) {
        All all = FastJsonUtils.readFile("data.json");
        List<Download> list = all.getDownload().getList();

        // 分配一个id,自增形式
        Long id = list.size() == 0 ? 1L : list.get(list.size() - 1).getId() + 1L;
        download.setId(id);

        // 对象中添加一条download信息
        list.add(download);

        // 总数加1
        Long total = all.getDownload().getTotal();
        all.getDownload().setTotal(++total);

        // 写回到文件里去
        String modifiedString = FastJsonUtils.toJSONString(all);
        FastJsonUtils.writeFile("data.json", modifiedString);
        return Result.success(null);
    }

    @SneakyThrows
    @Override
    public Result update(Download download) {
        All all = FastJsonUtils.readFile("data.json");
        List<Download> list = all.getDownload().getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(download.getId())) {
                list.set(i, download);
            }
        }
        String modifiedString = FastJsonUtils.toJSONString(all);
        FastJsonUtils.writeFile("data.json", modifiedString);
        return Result.success(null);
    }

    @SneakyThrows
    @Override
    public Result delete(Long id) {
        All all = FastJsonUtils.readFile("data.json");
        // 根据id删除一条download信息
        List<Download> list = all.getDownload().getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id))
                list.remove(list.get(i));
        }
        Long total = all.getDownload().getTotal();
        all.getDownload().setTotal(--total);
        // 写回到文件里去
        String modifiedString = FastJsonUtils.toJSONString(all);
        FastJsonUtils.writeFile("data.json", modifiedString);
        return Result.success(null);
    }

    @Override
    public Result getDownloadDirs() {
        All all = FastJsonUtils.readFile("data.json");
        List<Download> list = all.getDownload().getList();
        List<DirResult> downloadDirs = new ArrayList<>();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                DirResult temp = new DirResult();
                temp.setLabel(list.get(i).getPath());
                temp.setValue(i);
                downloadDirs.add(temp);
            }
        }
        return Result.success(downloadDirs);
    }
}
