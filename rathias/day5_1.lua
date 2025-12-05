local path = "inputs/day5_1.txt"

local function split(str, sep)
	sep = sep or ","
	local list = {}
	for s in string.gmatch(str, "([^" .. sep .. "]+)") do
		list[#list + 1] = s
	end
	return list
end

local freshRangesIds = {}
local addToFreshList = true
local nbFreshIngredients = 0
for line in io.lines(path) do
	if line == "" then
		addToFreshList = false
		goto continue
	end
	if addToFreshList then
		local freshRange = split(line, "-")
		table.insert(freshRangesIds, { tonumber(freshRange[1]), tonumber(freshRange[2]) })
	else
		for _, fresh in pairs(freshRangesIds) do
			if tonumber(line) >= fresh[1] and tonumber(line) <= fresh[2] then
				nbFreshIngredients = nbFreshIngredients + 1
				break
			end
		end
	end
	::continue::
end
print(nbFreshIngredients)
