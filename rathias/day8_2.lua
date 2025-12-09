local test = false
local path = "inputs/day8" .. (test and "_test" or "") .. ".txt"

local Point = { x = nil, y = nil, z = nil, connectedTo = nil, circuit = nil }
function Point:new(o)
	o = o or {} -- create object if user does not provide one
	setmetatable(o, self)
	self.__index = self
	return o
end
function Point:print()
	print(
		"{ x: "
			.. self.x
			.. ", y: "
			.. self.y
			.. ", z: "
			.. self.z
			.. " }"
			.. (self.circuit and " Circuit : " .. self.circuit or "")
	)
end

local function distance(p1, p2)
	return math.sqrt((p1.x - p2.x) ^ 2 + (p1.y - p2.y) ^ 2 + (p1.z - p2.z) ^ 2)
end

local function getAllDist(points)
	local distances = {}
	for k = 1, #points - 1 do
		for k2 = k + 1, #points do
			local currentDist = distance(points[k], points[k2])
			table.insert(distances, { p1 = points[k], p2 = points[k2], distance = currentDist })
		end
	end
	return distances
end

local function mergeCircuit(points, p1, p2)
	if p1.circuit == p2.circuit then
		return
	end
	local circuitToChange = p2.circuit
	for k, point in pairs(points) do
		if point.circuit == circuitToChange then
			points[k].circuit = p1.circuit
		end
	end
end

local function split(str, sep)
	sep = sep or ","
	local list = {}
	for s in string.gmatch(str, "([^" .. sep .. "]+)") do
		list[#list + 1] = s
	end
	return list
end

local points = {}
local circuitId = 1
for line in io.lines(path) do
	local lineSplit = split(line, ",")
	table.insert(
		points,
		Point:new({
			x = tonumber(lineSplit[1]),
			y = tonumber(lineSplit[2]),
			z = tonumber(lineSplit[3]),
			connectedTo = {},
			circuit = circuitId,
		})
	)
	circuitId = circuitId + 1
end

local distances = getAllDist(points)
table.sort(distances, function(a, b)
	return a.distance < b.distance
end)

local i = 0
repeat
	print("STEP : " .. i)
	local p1, p2 = distances[1].p1, distances[1].p2
	mergeCircuit(points, p1, p2)
	local isSameCircuit = true
	local circuit = points[1].circuit
	for _, point in ipairs(points) do
		if circuit ~= point.circuit then
			isSameCircuit = false
			break
		end
	end
	table.remove(distances, 1)
	i = i + 1
	if isSameCircuit then
		print("EEEEEENNNNNND")
		p1:print()
		p2:print()
		print(p1.x * p2.x)
	end
until isSameCircuit
