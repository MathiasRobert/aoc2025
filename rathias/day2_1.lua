local path = "../inputs/day2_1.txt"
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
	if #first == #last and #first % 2 ~= 0 and last % 2 ~= 0 then
		goto continue
	end
	-- print(first .. "-" .. last)
	for nb = tonumber(first), tonumber(last) do
		local nbStr = tostring(nb)
		if #nbStr % 2 == 0 then
			if string.sub(nbStr, 1, #nbStr / 2) == string.sub(nbStr, #nbStr / 2 + 1) then
				invalidIds = invalidIds + nb
				invalidList[#invalidList + 1] = nb
			end
		end
	end
	::continue::
end

print("===============")
-- print(table.unpack(invalidList))
print(invalidIds)
