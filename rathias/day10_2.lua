local test = false
local path = "inputs/day10" .. (test and "_test" or "") .. ".txt"

local function getLowestIndex(t, state)
	local index, min = nil, math.huge
	for k, v in pairs(t) do
		if v < min and state[k] ~= v then
			index = k
			min = v
		end
	end
	return index
end

local function getBtnsToPress(t, index, state, joltages)
	local btnsToPress = {}
	for i = 1, #t do
		local found = false
		local btnHaveOtherJoltageComplete = false
		for _, v2 in pairs(t[i]) do
			if state[v2] == joltages[v2] then
				-- print("ALREADY FULL")
				btnHaveOtherJoltageComplete = true
				break
			end
			if v2 == index then
				found = true
			end
		end
		if found == true and btnHaveOtherJoltageComplete == false then
			table.insert(btnsToPress, t[i])
		end
	end
	table.sort(btnsToPress, function(a, b)
		return #a > #b
	end)
	return btnsToPress
end

local function split(str, sep)
	sep = sep or ","
	local list = {}
	for s in string.gmatch(str, "([^" .. sep .. "]+)") do
		list[#list + 1] = s
	end
	return list
end

function table.copy(t)
	local t2 = {}
	for k, v in pairs(t) do
		t2[k] = v
	end
	return t2
end
function table.equal(t1, t2)
	for k, _ in pairs(t1) do
		if t1[k] ~= t2[k] then
			return false
		end
	end
	return true
end

local function pushButton(button, state)
	local newState = table.copy(state)
	for k, v in ipairs(button) do
		newState[v] = newState[v] + 1
		-- print("Button ind : " .. v .. " CurrSwitch : " .. currSwith)
	end
	return newState
end

local function isNewStateAboveJoltage(joltages, newState)
	for k, v in pairs(newState) do
		if v > joltages[k] then
			return true
		end
	end
	return false
end

local memoize = {}
setmetatable(memoize, { __mode = "v" }) -- make values weak

local function recPushButton(joltages, buttons, state, nbPushed, currentMin)
	-- local key = table.concat(joltages, ",") .. table.concat(state, ",")
	-- if memoize[key] then
	-- print("memoize")
	-- return true, memoize[key]
	-- end

	if table.equal(joltages, state) then
		-- print("FOUND")
		return true, nbPushed
	end
	if #buttons == 0 then
		-- print("END OF BUTTONS")
		return false
	end
	if currentMin and currentMin < nbPushed then
		-- print("CURRENT MIN DEPASSER")
		return false
	end
	local found = false
	local finalNbPushed
	local indexToCheck = getLowestIndex(joltages, state)
	local btnsToPress = getBtnsToPress(buttons, indexToCheck, joltages, state)
	-- print("indexToCheck : " .. indexToCheck)
	-- print(table.concat(btnsToPress[1], ","))

	for i = 1, #btnsToPress do
		-- print("State : " .. table.concat(state, ","))
		-- print("Push : " .. table.concat(btnsToPress[i], ","))
		local newState = pushButton(btnsToPress[i], state)
		-- print("NewState : " .. table.concat(newState, ","))
		if isNewStateAboveJoltage(joltages, newState) then
			-- print("ABOVE JOLTAGE")
			goto continue
		end
		found, finalNbPushed = recPushButton(joltages, buttons, newState, nbPushed + 1, currentMin)
		if found == true then
			if currentMin == nil or finalNbPushed < currentMin then
				-- memoize[key] = finalNbPushed
				currentMin = finalNbPushed
				return true, currentMin
			end
		end

		::continue::
	end
	return false, currentMin
end

local joltages, buttons = {}, {}
local lineNb = 0
for line in io.lines(path) do
	lineNb = lineNb + 1
	local lineSplit = split(line, " ")
	for k, v in ipairs(lineSplit) do
		if v:sub(1, 1) == "(" then
			buttons[lineNb] = buttons[lineNb] or {}
			local buttonsSplit = split(v:sub(2, #v - 1), ",")
			for ind, val in pairs(buttonsSplit) do
				buttonsSplit[ind] = tonumber(val) + 1
			end
			table.insert(buttons[lineNb], buttonsSplit)
		end
		if v:sub(1, 1) == "{" then
			local joltagesSplit = split(v:sub(2, #v - 1), ",")
			for ind, val in pairs(joltagesSplit) do
				joltagesSplit[ind] = tonumber(val)
			end
			table.insert(joltages, joltagesSplit)
		end
	end
end

local total = 0
for i = 1, #joltages do
	local state = {}
	for j = 1, #joltages[i] do
		table.insert(state, 0)
	end
	print(i .. "/" .. #joltages)
	print("Joltages: { " .. table.concat(joltages[i], ",") .. " }")
	local found, nbPushedMin = recPushButton(joltages[i], buttons[i], state, 0)
	print("Min pushes: " .. nbPushedMin)
	total = total + nbPushedMin
end
print("TOTAL : " .. total)
