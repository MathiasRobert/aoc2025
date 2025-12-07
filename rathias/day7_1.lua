local path = "inputs/day7.txt"

local total = 0
local beams = {}
for line in io.lines(path) do
	if line:find("S") then
		beams[line:find("S")] = true
		goto continue
	end

	for beam in line:gmatch("()^") do
		if beams[beam] == true then
			if beam > 0 then
				beams[beam - 1] = true
			end
			if beam < #line then
				beams[beam + 1] = true
			end
			total = total + 1
			beams[beam] = nil
		end
	end

	::continue::

	local linePrint = line
	for k, v in pairs(beams) do
		if linePrint:sub(k, k) == "." then
			linePrint = linePrint:sub(1, k - 1) .. "|" .. linePrint:sub(k + 1)
		end
	end
	print(linePrint)
end
print(total)
