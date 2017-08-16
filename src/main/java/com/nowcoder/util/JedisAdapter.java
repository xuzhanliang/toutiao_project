package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.List;

/**
 * Created by xyuser on 2017/5/8.
 */
@Service
public class JedisAdapter implements InitializingBean{
    private static final Logger logger  = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);
    }

    private Jedis getJedis(){
        return pool.getResource();
    }
    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("sadd发生异常 "+ e.getMessage());
            return 0;
        }finally {
            if(jedis !=null){
                jedis.close();
            }
        }
    }
    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("srem发生异常 "+ e.getMessage());
            return 0;
        }finally {
            if(jedis !=null){
                jedis.close();
            }
        }
    }
    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("sismember发生异常 "+ e.getMessage());
            return false;
        }finally {
            if(jedis !=null){
                jedis.close();
            }
        }
    }
    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("scard发生异常 "+ e.getMessage());
            return 0;
        }finally {
            if(jedis !=null){
                jedis.close();
            }
        }
    }

    public void set(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.set(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public String get(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return getJedis().get(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


    public void setObject(String key,Object obj){
        set(key, JSON.toJSONString(obj));
    }
    public <T> T getObject(String key,Class<T> clazz){
        String value = get(key);
        if(value!=null){
            return JSON.parseObject(value,clazz);
        }
        return null;
    }

    public long lpush(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.brpop(timeout,key);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


    /*
    public static void print(int index,Object obj){
        System.out.println(String.format("%d,%s",index,obj.toString()));
    }
    public static void main(String[] argv){
        Jedis jedis = new Jedis();
        jedis.flushAll();

        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello","newhello");
        //还可以设定过期时间
        jedis.setex("hello2",15,"world");

        //
        jedis.set("pv","100");
        jedis.incr("pv");
        print(2,jedis.get("pv"));
        jedis.incrBy("pv",5);
        print(3,jedis.get("pv"));

        //列表操作
        String listName = "listA";
        for(int i=0;i<10;i++){
            //从左边push进去
            jedis.lpush(listName,"a"+String.valueOf(i));
        }
        print(4,jedis.lrange(listName,0,12));
        print(5,jedis.llen(listName));
        print(6,jedis.lpop(listName));
        print(7,jedis.llen(listName));
        print(8,jedis.lindex(listName ,3));
        print(9,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a4","xx"));
        print(10,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"a4","xx"));
        print(11,jedis.lrange(listName,0,12));

        //hash
        String userKey = "user12";
        jedis.hset(userKey,"name","jim");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","15914503088");
        jedis.hset(userKey,"school","scut");
        print(12,jedis.hget(userKey,"name"));
        print(13,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(14,jedis.hgetAll(userKey));
        print(15,jedis.hkeys(userKey));
        print(16,jedis.hvals(userKey));
        print(17,jedis.hexists(userKey,"email"));
        print(18,jedis.hexists(userKey,"age"));
        jedis.hsetnx(userKey,"phone","123");
        jedis.hsetnx(userKey,"name","xxx");
        print(19,jedis.hgetAll(userKey));

        //set
        String likeKeys1="newsLike1";
        String likeKeys2="newsLike2";
        for(int i=0;i<10;i++){
            jedis.sadd(likeKeys1,String.valueOf(i));
            jedis.sadd(likeKeys2,String.valueOf(i*2));
        }
        print(20,jedis.smembers(likeKeys1));
        print(21,jedis.smembers(likeKeys2));

        print(22,jedis.sinter(likeKeys1,likeKeys2));
        print(23,jedis.sunion(likeKeys1,likeKeys2));
        print(24,jedis.sdiff(likeKeys1,likeKeys2));
        jedis.srem(likeKeys1,"5");
        print(25,jedis.sismember(likeKeys1,"5"));
        print(26,jedis.scard(likeKeys1));
        jedis.smove(likeKeys2,likeKeys1,"14");
        print(27,jedis.scard(likeKeys1));
        print(28,jedis.smembers(likeKeys1));

        //Sorted set
        String rankKey="rankKey";
        jedis.zadd(rankKey,15,"jim");
        jedis.zadd(rankKey,60,"ben");
        jedis.zadd(rankKey,75,"tom");
        jedis.zadd(rankKey,100,"mary");
        jedis.zadd(rankKey,40,"lucy");
        print(29,jedis.zcard(rankKey));
        print(30,jedis.zcount(rankKey,60,100));
        print(31,jedis.zscore(rankKey,"tom"));
        print(32,jedis.zincrby(rankKey,2,"lucy"));
        for(Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,"0","100"))
            print(33,tuple.getElement() + ":" + String.valueOf(tuple.getScore()));

        print(34,jedis.zrange(rankKey,0,2));
        print(35,jedis.zrevrange(rankKey,0,2));
        print(36,jedis.zrank(rankKey,"tom"));
        print(37,jedis.zrevrank(rankKey,"tom"));

        JedisPool pool = new JedisPool();
        for(int i=0;i<100;i++){
            Jedis j = pool.getResource();
            j.get("a");
            System.out.println("POOL" + i);
            j.close();
        }

    }
    */
}
