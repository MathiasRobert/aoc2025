local path = "inputs/day4_1.txt"

local matrix = {}
local index = 1
local lineSize = 0
for line in io.lines(path) do
	for j = 1, #line do
		matrix[(index - 1) * #line + j] = line:sub(j, j)
	end
	index = index + 1
	lineSize = #line
end

local accessible = 0
for k, v in ipairs(matrix) do
	if v == "@" then
		local rolls = 0
		for i = -1, 1 do
			for j = -1, 1 do
				if i == 0 and j == 0 then
					goto continue
				end
				local y, x = math.ceil(k / lineSize) + i, ((k - 1) % lineSize) + 1 - j
				if
					x > 0
					and y > 0
					and x <= lineSize
					and y <= #matrix / lineSize
					and (matrix[(y - 1) * lineSize + x] == "@" or matrix[(y - 1) * lineSize + x] == "x")
				then
					rolls = rolls + 1
				end
				if rolls == 4 then
					break
				end
				::continue::
			end
			if rolls == 4 then
				break
			end
		end
		if rolls < 4 then
			matrix[k] = "x"
			accessible = accessible + 1
		end
	end
end
print(accessible)
