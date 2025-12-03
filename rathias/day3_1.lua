local path = "../inputs/day3_1.txt"

local totalJoltage = 0
for line in io.lines(path) do
	local firstDigit, secondDigit = 0, 0
	for i = 9, 1, -1 do
		local firstIndex = string.find(line:sub(1, #line - 1), tostring(i))
		if firstIndex then
			firstDigit = i
			for j = 9, 1, -1 do
				local secondIndex = string.find(line:sub(firstIndex + 1), tostring(j))
				if secondIndex then
					secondDigit = j
					break
				end
			end
			break
		end
	end
	totalJoltage = totalJoltage + tonumber(firstDigit .. secondDigit)
	-- print(firstDigit .. " " .. secondDigit)
end
print(totalJoltage)
