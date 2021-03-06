package com.zjc.seckilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.mapper.SeckillOrderMapper;
import com.zjc.seckilldemo.pojo.SeckillOrder;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvaho
 * @since 2021-12-12
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 获取秒杀结果
     *
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public Integer getResult(User user, Integer goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new
                QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id",
                goodsId));
        if (null != seckillOrder) {
            return seckillOrder.getOrderId();
        } else {
            if (redisTemplate.hasKey("isStockEmpty:" + goodsId)) {
                return -1;
            }else {
                return 0;
            }
        }
    }

}
