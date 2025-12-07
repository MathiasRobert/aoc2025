local path = "inputs/day7.txt"

function table.copy(t)
	local t2 = {}
	for k, v in pairs(t) do
		t2[k] = v
	end
	return t2
end

local memoize = {}
setmetatable(memoize, { __mode = "v" }) -- make values weak

local function splitAndContinue(startIndex, lines, beams, timelines)
	local key = startIndex
	for k, _ in pairs(beams) do
		key = key .. "-" .. k
	end
	if memoize[key] then
		print("memoize")
		return memoize[key]
	end

	local currentTimelines = timelines
	for k = startIndex, #lines do
		local found = false
		for beam in lines[k]:gmatch("()^") do
			if beams[beam] == true then
				beams[beam] = nil
				if beam > 0 then
					local newBeams = table.copy(beams)
					newBeams[beam - 1] = true
					-- print(line:sub(1, beam - 2) .. "|" .. line:sub(beam))
					timelines = timelines + splitAndContinue(k + 1, lines, newBeams, currentTimelines)
				end
				if beam < #lines[k] then
					local newBeams = table.copy(beams)
					newBeams[beam + 1] = true
					-- print(line:sub(1, beam) .. "|" .. line:sub(beam + 2))
					timelines = timelines + splitAndContinue(k + 1, lines, newBeams, currentTimelines)
				end
				found = true
			end
		end
		if found then
			memoize[key] = timelines
			return timelines
		end
		local linePrint = lines[k]
		for k, v in pairs(beams) do
			if linePrint:sub(k, k) == "." then
				linePrint = linePrint:sub(1, k - 1) .. "|" .. linePrint:sub(k + 1)
			end
		end
		print(linePrint)
	end
	print("END")
	return timelines + 1
end

local beams, lines = {}, {}
for line in io.lines(path) do
	if line:find("S") then
		beams[line:find("S")] = true
		-- print(line)
	else
		table.insert(lines, line)
	end
end
local timelines = splitAndContinue(1, lines, beams, 0)
print(timelines)
