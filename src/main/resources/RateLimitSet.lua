local key = KEYS[1] 			--限流KEY
local limit = tonumber(ARGV[1]) --容量
-- 获取当前令牌数
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then --如果超出容量
    return 0
else
	redis.call("INCRBY", key, "1") --令牌数+1
end
return 1  --返回1代表不限流