package com.workflow.service.impl;

import com.workflow.model.params.PageParam;
import com.workflow.pojo.FailedHandler;
import com.workflow.pojo.json.All;
import com.workflow.service.FailedHandlerService;
import com.workflow.util.FastJsonUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import vo.DirResult;
import vo.PageResult;
import vo.Result;

import java.util.ArrayList;
import java.util.List;

@Service
public class FailedHandlerServiceImpl implements FailedHandlerService {

    @Override
    public Result listFailedHandler(PageParam pageParam) {
        Integer currentPage = pageParam.getCurrentPage();
        Integer pageSize = pageParam.getPageSize();

        All all = FastJsonUtils.readFile("data.json");
        List<FailedHandler> list = all.getFailedHandler().getList();
        PageResult<FailedHandler> pageResult = new PageResult<>();

        if (list.size() == 0) {
            pageResult.setList(null);
        }

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = startIndex + pageSize - 1;
        List<FailedHandler> temp = new ArrayList<>();
        for (int i = startIndex; i < list.size() && i <= endIndex; i++) {
            temp.add(list.get(i));
        }
        pageResult.setList(temp);
        pageResult.setTotal(all.getFailedHandler().getTotal());
        return Result.success(pageResult);

    }

    @SneakyThrows
    @Override
    public Result add(FailedHandler failedHandler) {
        All all = FastJsonUtils.readFile("data.json");
        List<FailedHandler> list = all.getFailedHandler().getList();

        // 分配一个id,自增形式
        Long id = list.size() == 0 ? 1L : list.get(list.size() - 1).getId() + 1L;
        failedHandler.setId(id);

        // 对象中添加一条failedHandler信息
        list.add(failedHandler);

        // 总数加1
        Long total = all.getFailedHandler().getTotal();
        all.getFailedHandler().setTotal(++total);

        // 写回到文件里去
        String modifiedString = FastJsonUtils.toJSONString(all);
        FastJsonUtils.writeFile("data.json", modifiedString);
        return Result.success(null);
    }

    @SneakyThrows
    @Override
    public Result update(FailedHandler failedHandler) {
        All all = FastJsonUtils.readFile("data.json");
        List<FailedHandler> list = all.getFailedHandler().getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(failedHandler.getId())) {
                list.set(i, failedHandler);
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
        // 根据id删除一条failedHandler信息
        List<FailedHandler> list = all.getFailedHandler().getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id))
                list.remove(list.get(i));
        }
        Long total = all.getFailedHandler().getTotal();
        all.getFailedHandler().setTotal(--total);
        // 写回到文件里去
        String modifiedString = FastJsonUtils.toJSONString(all);
        FastJsonUtils.writeFile("data.json", modifiedString);
        return Result.success(null);
    }

    @Override
    public Result getFailedHandlerDirs() {
        All all = FastJsonUtils.readFile("data.json");
        List<FailedHandler> list = all.getFailedHandler().getList();
        List<DirResult> failedHandlerDirs = new ArrayList<>();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                DirResult temp = new DirResult();
                temp.setLabel(list.get(i).getPath());
                temp.setValue(i);
                failedHandlerDirs.add(temp);
            }
        }
        Long recent = all.getFailedHandler().getRecent();
        Object[] res = new Object[]{recent,failedHandlerDirs};
        return Result.success(res);
    }
}
