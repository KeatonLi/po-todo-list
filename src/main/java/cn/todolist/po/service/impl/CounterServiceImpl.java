package cn.todolist.po.service.impl;

import cn.todolist.po.model.Counter;
import cn.todolist.po.service.CounterService;
import cn.todolist.po.mapper.CountersMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class CounterServiceImpl implements CounterService {

  @Resource
  CountersMapper countersMapper;

  @Override
  public Optional<Counter> getCounter(Integer id) {
    return Optional.ofNullable(countersMapper.getCounter(id));
  }

  @Override
  public void upsertCount(Counter counter) {
    countersMapper.upsertCount(counter);
  }

  @Override
  public void clearCount(Integer id) {
    countersMapper.clearCount(id);
  }
}
