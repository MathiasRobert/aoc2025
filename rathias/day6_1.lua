local path = "inputs/day6_1.txt"

local function split(str, sep)
	sep = sep or ","
	local list = {}
	for s in string.gmatch(str, "([^" .. sep .. "]+)") do
		list[#list + 1] = s
	end
	return list
end

local total = 0
local problems = {}
local i = 0
for line in io.lines(path) do
	if line:find("%d") then
		table.insert(problems, split(line, " "))
	else
		local operations = split(line, " ")
		for k, operator in ipairs(operations) do
			local result
			print("==========")
			for _, nbList in ipairs(problems) do
				if result then
					local expr = result .. operator .. nbList[k]
					print(expr)
					local problem = load("return " .. expr)
					result = problem()
					print(result)
				else
					result = nbList[k]
				end
			end
			total = total + result
		end
	end
end
print("Total : " .. total)
