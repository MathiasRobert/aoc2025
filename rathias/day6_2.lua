local path = "inputs/day6_1.txt"

local function getVerticalNb(problems, index)
	local nb = ""
	for k, v in ipairs(problems) do
		nb = nb .. v:sub(index, index)
	end
	return nb
end

local total = 0
local problems = {}
for line in io.lines(path) do
	if line:find("%d") then
		table.insert(problems, line)
	else
		local nbsToCalc = {}
		local operator = line:sub(1, 1)
		print(operator)
		for pos = 1, #line + 1 do
			print("pos" .. pos)
			local nextOperator = line:sub(pos + 1, pos + 1)
			if nextOperator ~= " " and pos ~= #line then
				local result
				for _, nb in ipairs(nbsToCalc) do
					if result then
						local expr = result .. operator .. nb
						print(expr)
						local problem = load("return " .. expr)
						result = problem()
					else
						result = nb
					end
				end
				nbsToCalc = {}
				operator = nextOperator
				print("Result : " .. result)
				total = total + result
			else
				local verticalNb = getVerticalNb(problems, pos)
				table.insert(nbsToCalc, tonumber(verticalNb))
				print("Add " .. verticalNb)
			end
		end
	end
end
print("Total : " .. total)
