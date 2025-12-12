local test = false
local path = "inputs/day11" .. (test and "_test" or "") .. ".txt"

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

local nbDacFft = 0

local memoizeDac, memoizeFft, memoizeDacFft = {}, {}, {}
setmetatable(memoizeDac, { __mode = "v" }) -- make values weak
setmetatable(memoizeFft, { __mode = "v" }) -- make values weak
setmetatable(memoizeDacFft, { __mode = "v" }) -- make values weak

local memoize = {}
setmetatable(memoizeDacFft, { __mode = "v" }) -- make values weak

local function shouldContinue(state, currNode)
	local stateSplit = split(state, ",")
	table.sort(stateSplit, function(a, b)
		return a:lower() < b:lower()
	end)
	local key = ""
	for _, value in ipairs(stateSplit) do
		key = key .. value
	end
	key = key .. "-" .. currNode
	if memoize[key] then
		return false
	end
	return true
	-- if memoizeDacFft[currNode] then
	-- 	-- print("memoizeDacFft")
	-- 	nbDacFft = nbDacFft + memoizeDacFft[currNode]
	-- 	return false
	-- elseif memoizeDac[currNode] ~= false and memoizeDac[currNode] ~= nil and state:match("fft") == "fft" then
	-- 	-- print("memoizeDac")
	-- 	nbDacFft = nbDacFft + memoizeDac[currNode]
	-- 	return false
	-- elseif memoizeFft[currNode] ~= false and memoizeFft[currNode] ~= nil and state:match("dac") == "dac" then
	-- 	-- print("memoizeFft")
	-- 	nbDacFft = nbDacFft + memoizeFft[currNode]
	-- 	return false
	-- end
	-- if state:match("dac") == "dac" and state:match("fft") == "fft" then
	-- 	return true
	-- elseif state:match("dac") == "dac" and memoizeFft[currNode] == false then
	-- 	return false
	-- elseif state:match("fft") == "fft" and memoizeDac[currNode] == false then
	-- 	return false
	-- elseif memoizeDac[currNode] == false and memoizeFft[currNode] == false then
	-- 	return false
	-- end
	-- return true
end

local function getPaths(endNode, devices, currNode, state, paths)
	if shouldContinue(state, currNode) == false then
		-- print("memoize")
		return false, paths
	end
	print(state)

	--
	state = state or ""
	paths = paths or {}

	if currNode == endNode then
		-- print("PATH FOUND : " .. state)
		table.insert(paths, state)
		local found = 0
		if state:match("dac") == "dac" and state:match("fft") == "fft" then
			nbDacFft = nbDacFft + 1
			found = 1
		end
		return true, paths, found
	end
	local stillDevices = false
	for _, _ in pairs(devices) do
		stillDevices = true
	end
	-- if stillDevices == false then
	-- print("CUL DE SAC")
	-- return false, paths
	-- end

	local found = false
	local neighbors = devices[currNode]
	if not neighbors then
		print("ALREADY VISITED")
		return false, paths
	end
	local lastPathes = {}
	local nbFoundsTotal, nbFounds = 0, 0
	for _, neighbor in pairs(neighbors) do
		-- print("State : " .. state)
		-- print("Push :")
		local newState = state .. "," .. neighbor
		-- print("NewState : " .. newState)
		local newDevices = table.copy(devices)
		newDevices[currNode] = nil
		found, paths, nbFounds = getPaths(endNode, newDevices, neighbor, newState, paths)
		if found == true then
			nbFoundsTotal = nbFoundsTotal + nbFounds
			table.insert(lastPathes, paths[#paths])
		end
	end
	if #lastPathes == 0 then
		local stateSplit = split(state, ",")
		table.sort(stateSplit, function(a, b)
			return a:lower() < b:lower()
		end)
		local key = ""
		for _, value in ipairs(stateSplit) do
			key = key .. value
		end
		key = key .. "-" .. currNode
		memoize[key] = true
	end
	-- for _, lastPath in pairs(lastPathes) do
	-- 	local indexCurr = lastPath:find(currNode)
	-- 	local matchDac = lastPath:sub(indexCurr + 3):match("dac")
	-- 	local matchFft = lastPath:sub(indexCurr + 3):match("fft")
	-- 	if matchDac ~= "dac" then
	-- 		memoizeDac[currNode] = false
	-- 	end
	-- 	if matchFft ~= "fft" then
	-- 		memoizeFft[currNode] = false
	-- 	end
	-- 	if matchDac == "dac" and matchFft == "fft" then
	-- 		if memoizeDacFft[currNode] then
	-- 			memoizeDacFft[currNode] = memoizeDacFft[currNode] + nbFoundsTotal
	-- 		else
	-- 			memoizeDacFft[currNode] = nbFoundsTotal
	-- 		end
	-- 	else
	-- 		if matchDac == "dac" then
	-- 			memoizeDac[currNode] = memoizeDac[currNode] and memoizeDac[currNode] + nbFoundsTotal or nbFoundsTotal
	-- 		end
	-- 		if matchFft == "fft" then
	-- 			memoizeFft[currNode] = memoizeFft[currNode] and memoizeFft[currNode] + nbFoundsTotal or nbFoundsTotal
	-- 		end
	-- 	end
	-- end
	return found, paths, nbFoundsTotal
end

local devices = {}
local lineNb = 0
for line in io.lines(path) do
	lineNb = lineNb + 1
	local lineSplit = split(line, " ")
	local key = ""
	for k, v in ipairs(lineSplit) do
		if k == 1 then
			key = v:sub(1, #v - 1)
			devices[key] = {}
		else
			table.insert(devices[key], v)
		end
	end
end

local _, paths = getPaths("out", devices, "svr", "svr")
-- print(#paths)
print(nbDacFft)
