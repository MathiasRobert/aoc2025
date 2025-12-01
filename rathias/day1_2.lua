local path = "../inputs/day1_1.txt"
local input = io.open(path, "rb")

local dial = 50
local dialHitZero = 0

for line in io.lines(path) do
	local direction = string.sub(line, 1, 1)
	local amount = tonumber(string.sub(line, 2))
	local newDial = dial
	if direction == "L" then
		newDial = (newDial - amount)
	elseif direction == "R" then
		newDial = (newDial + amount)
	end
	local fullTurns = (amount // 100)
	dialHitZero = dialHitZero + fullTurns
	newDial = (newDial % 100)
	if newDial == 0 then
		dialHitZero = dialHitZero + 1
	elseif dial ~= 0 then
		if direction == "L" and newDial > dial then
			dialHitZero = dialHitZero + 1
		end
		if direction == "R" and newDial < dial then
			dialHitZero = dialHitZero + 1
		end
	end
	print("--" .. dial .. " " .. line .. " " .. newDial .. "--" .. dialHitZero)
	dial = newDial
end
print(dialHitZero)
