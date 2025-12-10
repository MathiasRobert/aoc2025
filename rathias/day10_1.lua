local test = false
local path = "inputs/day10" .. (test and "_test" or "") .. ".txt"

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

local memoize = {}
setmetatable(memoize, { __mode = "v" }) -- make values weak

local function pushButton(button, state)
	local newState = state
	for k, v in ipairs(button) do
		local currSwith = state:sub(v + 1, v + 1)
		-- print("Button ind : " .. v .. " CurrSwitch : " .. currSwith)
		newState = newState:sub(1, v) .. (currSwith == "#" and "." or "#") .. newState:sub(v + 2)
	end
	return newState
end

local function recPushButton(switches, buttons, state, nbPushed, currentMin)
	-- local key = startIndex
	-- for k, _ in pairs(beams) do
	-- 	key = key .. "-" .. k
	-- end
	-- if memoize[key] then
	-- 	print("memoize")
	-- 	return memoize[key]
	-- end
	--
	if switches == state then
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
	local buttonsWithoutPrev = table.copy(buttons)
	for i = 1, #buttons do
		-- print("State : " .. state)
		-- print("Push :")
		local newState = pushButton(buttonsWithoutPrev[1], state)
		-- print("NewState : " .. newState)
		local newButtons = table.copy(buttonsWithoutPrev)
		table.remove(newButtons, 1)
		found, finalNbPushed = recPushButton(switches, newButtons, newState, nbPushed + 1, currentMin)
		if (found and not currentMin) or (found and found == true and finalNbPushed < currentMin) then
			currentMin = finalNbPushed
		end
		table.remove(buttonsWithoutPrev, 1)
	end
	return true, currentMin
end

local switches, buttons = {}, {}
local lineNb = 0
for line in io.lines(path) do
	lineNb = lineNb + 1
	local lineSplit = split(line, " ")
	for k, v in ipairs(lineSplit) do
		if k == 1 then
			table.insert(switches, v:sub(2, #v - 1))
		end
		if v:sub(1, 1) == "(" then
			buttons[lineNb] = buttons[lineNb] or {}
			local buttonsSplit = split(v:sub(2, #v - 1), ",")
			table.insert(buttons[lineNb], buttonsSplit)
		end
	end
end

local total = 0
for i = 1, #switches do
	local state = ""
	for j = 1, #switches[i] do
		state = state .. "."
	end
	print(i .. "/" .. #switches)
	local found, nbPushedMin = recPushButton(switches[i], buttons[i], state, 0)
	print("Min pushes: " .. nbPushedMin)
	total = total + nbPushedMin
end
print("TOTAL : " .. total)
