local test = true
local path = "inputs/day8" .. (test and "_test" or "") .. ".txt"

local circuitId = 1
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
function Point:connect(p1, p2)
	table.insert(p1.connectedTo, p2)
	table.insert(p2.connectedTo, p1)
	print("P1:")
	p1:print()
	print("connectedTo")
	for _, p in ipairs(p1.connectedTo) do
		p:print()
	end
	print("P2:")
	p2:print()
	print("connectedTo")
	for _, p in ipairs(p2.connectedTo) do
		p:print()
	end
end
function Point:equal(point)
	if point.x == self.x and point.y == self.y and point.z == self.z then
		return true
	end
	return false
end
function Point:isConnectedTo(point)
	for _, p2 in ipairs(self.connectedTo) do
		if p2:equal(point) then
			return true
		end
	end
	return false
end

local function distance(p1, p2)
	return math.sqrt((p1.x - p2.x) ^ 2 + (p1.y - p2.y) ^ 2 + (p1.z - p2.z) ^ 2)
end
local function getShorterDist(points)
	local p1, p2, shortestDist
	for k = 1, #points do
		for k2 = k + 1, #points do
			if points[k]:isConnectedTo(points[k2]) then
				goto continue
			end
			local currentDist = distance(points[k], points[k2])
			if not shortestDist or shortestDist > currentDist then
				p1, p2, shortestDist = points[k], points[k2], currentDist
			end
			::continue::
		end
	end
	return p1, p2, shortestDist
end
local function getAllDist(points)
	local distances = {}
	for k = 1, #points do
		for k2 = k + 1, #points do
			local currentDist = distance(points[k], points[k2])
			table.insert(distances, { p1 = points[k], p2 = points[k2], distance = currentDist })
		end
	end
	return distances
end
local function getShorterAllDist(distances)
	for _, v in pairs(distances) do
		if not v.p1:isConnectedTo(v.p2) then
			-- if v.p1.circuit and v.p1.circuit == v.p2.circuit then
			-- 	goto continue
			-- else
			return v.p1, v.p2, v.distance
		end
		-- ::continue::
	end
end

local function createOrMergeCircuit(points, p1, p2)
	if not p1.circuit and not p2.circuit then
		p1.circuit = circuitId
		p2.circuit = circuitId
		circuitId = circuitId + 1
	elseif p1.circuit and not p2.circuit then
		p2.circuit = p1.circuit
	elseif p2.circuit and not p1.circuit then
		p1.circuit = p2.circuit
	elseif p1.circuit and p2.circuit then
		if p1.circuit == p2.circuit then
			return
		end
		for _, point in ipairs(points) do
			if point.circuit == p2.circuit then
				point.circuit = p1.circuit
			end
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
for line in io.lines(path) do
	local lineSplit = split(line, ",")
	table.insert(
		points,
		Point:new({
			x = tonumber(lineSplit[1]),
			y = tonumber(lineSplit[2]),
			z = tonumber(lineSplit[3]),
			connectedTo = {},
		})
	)
end

local distances = getAllDist(points)
table.sort(distances, function(a, b)
	return a.distance < b.distance
end)
print(#distances)
for k, v in pairs(distances) do
	print(v.distance)
end

for i = 1, (test and 10 or 1000) do
	-- local p1, p2, dist = getShorterDist(points)
	local p1, p2, dist = getShorterAllDist(distances)
	Point:connect(p1, p2)
	createOrMergeCircuit(points, p1, p2)
	-- p1:print()
	-- p2:print()
	-- print("===========")
	print(i)
end

local circuits = {}
for _, point in ipairs(points) do
	point:print()
	if point.circuit then
		circuits[point.circuit] = (circuits[point.circuit] or 0) + 1
	end
end
local circuitsSorted = {}
for k, n in pairs(circuits) do
	table.insert(circuitsSorted, n)
end
table.sort(circuitsSorted, function(a, b)
	return a > b
end)

local result = 1
for k, v in pairs(circuitsSorted) do
	print("Circuit " .. k .. " - Nb " .. v)
	if k < 4 then
		result = result * v
	end
end
print(result)
