local path = "../inputs/day3_1.txt"

local function getHighestJoltage(str, startIndex, endIndex)
	for digit = 9, 1, -1 do
		local index = string.find(str:sub(startIndex, endIndex), tostring(digit))
		if index then
			return index, digit
		end
	end
end

local totalJoltage = 0
for line in io.lines(path) do
	local joltage = ""
	local startIndex = 1
	for i = 1, 12 do
		local index, digit = getHighestJoltage(line, startIndex, #line - 12 + i)
		startIndex = startIndex + index
		joltage = joltage .. digit
	end
	totalJoltage = totalJoltage + tonumber(joltage)
end
print(totalJoltage)
