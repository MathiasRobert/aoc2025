local path = "../inputs/day2_test.txt"
local file = assert(io.open(path, "rb"))
local input = file:read("*a")

local function split(str, sep)
	sep = sep or ","
	local list = {}
	for s in string.gmatch(str, "([^" .. sep .. "]+)") do
		list[#list + 1] = s
	end
	return list
end

local ranges = split(input, ",")
local invalidIds = 0
local invalidList = {}
for i, range in ipairs(ranges) do
	local first, last = table.unpack(split(range, "-"))
	for nb = tonumber(first), tonumber(last) do
		local nbStr = tostring(nb)
		print(nbStr)
		print(string.find(nbStr, "^(%d+)+$"))
		if string.find(nbStr, "^((%d+)+)$") then
			invalidIds = invalidIds + nb
			invalidList[#invalidList + 1] = nb
		end
	end
end

print("===============")
-- print(table.unpack(invalidList))
print(invalidIds)
