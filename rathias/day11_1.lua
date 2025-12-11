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

local memoize = {}
setmetatable(memoize, { __mode = "v" }) -- make values weak

local function getPaths(devices, currNode, state, paths)
	-- local key = startIndex
	-- for k, _ in pairs(beams) do
	-- 	key = key .. "-" .. k
	-- end
	-- if memoize[key] then
	-- 	print("memoize")
	-- 	return memoize[key]
	-- end
	--
	--
	state = state or ""
	paths = paths or {}

	if currNode == "out" then
		print("PATH FOUND : " .. state .. currNode)
		table.insert(paths, state)
		return true, paths
	end
	local stillDevices = false
	for _, _ in pairs(devices) do
		stillDevices = true
	end
	if stillDevices == false then
		print("CUL DE SAC")
		return false, paths
	end
	print("LOL")

	local found = false
	local neighbors = devices[currNode]
  if not neighbors then
    print("ALREADY VISITED")
    return false, paths
  end
	for _, neighbor in pairs(neighbors) do
		print("State : " .. state)
		-- print("Push :")
		local newState = state .. "," .. neighbor
		print("NewState : " .. newState)
		local newDevices = table.copy(devices)
		newDevices[currNode] = nil
		found, paths = getPaths(newDevices, neighbor, newState, paths)
    ::continue::
	end
	return true, paths
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

local _, paths = getPaths(devices, "you", "you")
print(#paths)
