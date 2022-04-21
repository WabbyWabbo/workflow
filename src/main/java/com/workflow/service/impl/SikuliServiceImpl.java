package com.workflow.service.impl;

import com.workflow.model.params.PageParam;
import com.workflow.pojo.json.All;
import com.workflow.service.SikuliService;
import com.workflow.pojo.Sikuli;
import com.workflow.util.FastJsonUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import vo.DirResult;
import vo.PageResult;
import vo.Result;
import java.util.ArrayList;
import java.util.List;

@Service
public class SikuliServiceImpl implements SikuliService {

    @Override
    public Result listSikuli(PageParam pageParam) {
        Integer currentPage = pageParam.getCurrentPage();
        Integer pageSize = pageParam.getPageSize();

        All all = FastJsonUtils.readFile("data.json");
        List<Sikuli> list = all.getSikuli().getList();
        PageResult<Sikuli> pageResult = new PageResult<>();

        if (list.size() == 0) {
            pageResult.setList(null);
        }

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = startIndex + pageSize - 1;
        List<Sikuli> temp = new ArrayList<>();
        for (int i = startIndex; i < list.size() && i <= endIndex; i++) {
            temp.add(list.get(i));
        }
        pageResult.setList(temp);
        pageResult.setTotal(all.getSikuli().getTotal());
        return Result.success(pageResult);

    }

    @SneakyThrows
    @Override
    public Result add(Sikuli sikuli) {
        All all = FastJsonUtils.readFile("data.json");
        List<Sikuli> list = all.getSikuli().getList();

        // 分配一个id,自增形式
        Long id = list.size() == 0 ? 1L : list.get(list.size() - 1).getId() + 1L;
        sikuli.setId(id);

        // 对象中添加一条sikuli信息
        list.add(sikuli);

        // 总数加1
        Long total = all.getSikuli().getTotal();
        all.getSikuli().setTotal(++total);

        // 写回到文件里去
        String modifiedString = FastJsonUtils.toJSONString(all);
        FastJsonUtils.writeFile("data.json", modifiedString);
        return Result.success(null);
    }

    @SneakyThrows
    @Override
    public Result update(Sikuli sikuli) {
        All all = FastJsonUtils.readFile("data.json");
        List<Sikuli> list = all.getSikuli().getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(sikuli.getId())) {
                list.set(i, sikuli);
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
        // 根据id删除一条sikuli信息
        List<Sikuli> list = all.getSikuli().getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id))
                list.remove(list.get(i));
        }
        Long total = all.getSikuli().getTotal();
        all.getSikuli().setTotal(--total);
        // 写回到文件里去
        String modifiedString = FastJsonUtils.toJSONString(all);
        FastJsonUtils.writeFile("data.json", modifiedString);
        return Result.success(null);
    }

    @Override
    public Result getSikuliDirs() {
        All all = FastJsonUtils.readFile("data.json");
        List<Sikuli> list = all.getSikuli().getList();
        List<DirResult> sikuliDirs = new ArrayList<>();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                DirResult temp = new DirResult();
                temp.setLabel(list.get(i).getPath());
                temp.setValue(list.get(i).getPath());
                sikuliDirs.add(temp);
            }
        }
        return Result.success(sikuliDirs);
    }
}
