local test = false
local path = "inputs/day9" .. (test and "_test" or "") .. ".txt"

local Point = { x = nil, y = nil }
function Point:new(o)
	o = o or {} -- create object if user does not provide one
	setmetatable(o, self)
	self.__index = self
	return o
end
function Point:print()
	print("{ x: " .. self.x .. ", y: " .. self.y .. " }")
end

local function distance(p1, p2)
	return math.abs(p1.x - p2.x) + math.abs(p1.y - p2.y)
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
		})
	)
	circuitId = circuitId + 1
end

local distances = getAllDist(points)
table.sort(distances, function(a, b)
	return a.distance > b.distance
end)

local p1, p2 = distances[1].p1, distances[1].p2
p1:print()
p2:print()
local width = math.abs(p1.x - p2.x) + 1
local height = math.abs(p1.y - p2.y) + 1
print(distances[1].distance)
print("Width : " .. width .. ", Height : " .. height)
print("Area : " .. width * height)
