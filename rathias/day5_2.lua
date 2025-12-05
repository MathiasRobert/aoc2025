local path = "inputs/day5_test.txt"

local function split(str, sep)
	sep = sep or ","
	local list = {}
	for s in string.gmatch(str, "([^" .. sep .. "]+)") do
		list[#list + 1] = s
	end
	return list
end

local function findInRange(tbl, nb)
	for k, v in ipairs(tbl) do
		if nb >= v[1] and nb <= v[2] then
			return k, v
		end
	end
end
local function containInRange(tbl, first, last)
	local containedTbl = {}
	for k, v in ipairs(tbl) do
		if first <= v[1] and last >= v[2] then
			table.insert(containedTbl, { k, v })
		end
	end
	return containedTbl
end

local function mergeRangeIfNeeded(tbl, first, last)
	if #tbl > 0 then
		local containedRanges = containInRange(tbl, first, last)
		if containedRanges then
			for k, v in ipairs(containedRanges) do
				table.remove(tbl, v[1] - k + 1)
			end
		end
		local index1, value1 = findInRange(tbl, first)
		local index2, value2 = findInRange(tbl, last)
		if index1 and index2 then
			if index1 == index2 then
				table.remove(tbl, index1)
			elseif index1 < index2 then
				table.remove(tbl, index1)
				table.remove(tbl, index2 - 1)
			else
				table.remove(tbl, index2)
				table.remove(tbl, index1 - 1)
			end
			table.insert(tbl, { value1[1], value2[2] })
		elseif index1 then
			tbl[index1] = { value1[1], last }
		elseif index2 then
			tbl[index2] = { first, value2[2] }
		else
			table.insert(tbl, { first, last })
		end
	else
		table.insert(tbl, { first, last })
	end
end

local freshRangesIds = {}
local usedFreshIds = {}
for line in io.lines(path) do
	if line == "" then
		break
	end
	local freshRange = split(line, "-")
	local first, last = tonumber(freshRange[1]), tonumber(freshRange[2])
	mergeRangeIfNeeded(freshRangesIds, first, last)
	print("=======")
	for k, v in ipairs(freshRangesIds) do
		print(table.unpack(v))
	end
end

local total = 0
for k, v in ipairs(freshRangesIds) do
	total = total + v[2] - v[1] + 1
end
print("Total : " .. total)
