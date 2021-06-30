local key = KEYS[1] 			--限流KEY
-- 获取当前可用令牌数
local current = tonumber(redis.call('get', key) or "0")
if current <= 0 then --没有令牌了
    return 0
else
	redis.call("DECRBY", key, "1") --令牌数-1
end
return 1  --返回1代表不限流