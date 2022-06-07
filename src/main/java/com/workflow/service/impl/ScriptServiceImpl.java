package com.workflow.service.impl;

import com.workflow.model.params.PageParam;
import com.workflow.pojo.Script;
import com.workflow.pojo.json.All;
import com.workflow.service.ScriptService;
import com.workflow.util.FastJsonUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import vo.DirResult;
import vo.PageResult;
import vo.Result;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScriptServiceImpl implements ScriptService {

    @Override
    public Result listScript(PageParam pageParam) {
        Integer currentPage = pageParam.getCurrentPage();
        Integer pageSize = pageParam.getPageSize();

        All all = FastJsonUtils.readFile("data.json");
        List<Script> list = all.getScript().getList();
        PageResult<Script> pageResult = new PageResult<>();

        if (list.size() == 0) {
            pageResult.setList(null);
        }

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = startIndex + pageSize - 1;
        List<Script> temp = new ArrayList<>();
        for (int i = startIndex; i < list.size() && i <= endIndex; i++) {
            temp.add(list.get(i));
        }
        pageResult.setList(temp);
        pageResult.setTotal(all.getScript().getTotal());
        return Result.success(pageResult);

    }

    @SneakyThrows
    @Override
    public Result add(Script script) {
        All all = FastJsonUtils.readFile("data.json");
        List<Script> list = all.getScript().getList();

        // 分配一个id,自增形式
        Long id = list.size() == 0 ? 1L : list.get(list.size() - 1).getId() + 1L;
        script.setId(id);

        // 对象中添加一条script信息
        list.add(script);

        // 总数加1
        Long total = all.getScript().getTotal();
        all.getScript().setTotal(++total);

        // 写回到文件里去
        String modifiedString = FastJsonUtils.toJSONString(all);
        FastJsonUtils.writeFile("data.json", modifiedString);
        return Result.success(null);
    }

    @SneakyThrows
    @Override
    public Result update(Script script) {
        All all = FastJsonUtils.readFile("data.json");
        List<Script> list = all.getScript().getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(script.getId())) {
                list.set(i, script);
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
        // 根据id删除一条script信息
        List<Script> list = all.getScript().getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id))
                list.remove(list.get(i));
        }
        Long total = all.getScript().getTotal();
        all.getScript().setTotal(--total);
        // 写回到文件里去
        String modifiedString = FastJsonUtils.toJSONString(all);
        FastJsonUtils.writeFile("data.json", modifiedString);
        return Result.success(null);
    }

    @Override
    public Result getScriptsDirs() {
        All all = FastJsonUtils.readFile("data.json");
        List<Script> list = all.getScript().getList();
        List<DirResult> scriptDirs = new ArrayList<>();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                DirResult temp = new DirResult();
                temp.setLabel(list.get(i).getPath());
                temp.setValue(i);
                scriptDirs.add(temp);
            }
        }
        Long recent = all.getScript().getRecent();
        Object[] res = new Object[]{recent,scriptDirs};
        return Result.success(res);
    }
}
