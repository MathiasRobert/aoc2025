local path = "../inputs/day1_1.txt"
local input = io.open(path, "rb")

local dial = 50
local dialHitZero = 0

for line in io.lines(path) do
	local direction = string.sub(line, 1, 1)
	local newDial = dial
	if direction == "L" then
		newDial = (newDial - tonumber(string.sub(line, 2)))
	elseif direction == "R" then
		newDial = (newDial + tonumber(string.sub(line, 2)))
	end
	if newDial < 0 or newDial > 99 then
		newDial = (newDial % 100)
	end
	if newDial == 0 then
		dialHitZero = dialHitZero + 1
		newDial = 0
	end
	print("--" .. dial .. " " .. line .. " " .. newDial .. "--")
	dial = newDial
end
print(dialHitZero)
