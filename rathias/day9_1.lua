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

local function getDistance(p1, p2)
	return math.abs(p1.x - p2.x) + math.abs(p1.y - p2.y)
end

local function getAllDist(points)
	local distances = {}
	for k = 1, #points - 1 do
		for k2 = k + 1, #points do
			local currentDist = getDistance(points[k], points[k2])
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

local function getAdjacents(point, points)
	local dTop, dBot, dLeft, dRight
	for _, p in ipairs(points) do
		if p.x == point.x and p.y < point.y and ((not dTop) or dTop > point.y - p.y) then
			dTop = point.y - p.y
		end
		if p.x == point.x and p.y > point.y and ((not dBot) or dBot > p.y - point.y) then
			dBot = p.y - point.y
		end
		if p.y == point.y and p.x < point.x and ((not dLeft) or dLeft > point.x - p.x) then
			dLeft = point.x - p.x
		end
		if p.y == point.y and p.x > point.x and ((not dRight) or dRight > p.x - point.x) then
			dRight = p.x - point.x
		end
	end
	return dTop, dBot, dLeft, dRight
end

local reds = {}
for line in io.lines(path) do
	local lineSplit = split(line, ",")
	table.insert(
		reds,
		Point:new({
			x = tonumber(lineSplit[1]) + 1,
			y = tonumber(lineSplit[2]) + 1,
		})
	)
end

local greens = {}
local shapeTest = {}
for _, red in ipairs(reds) do
	table.insert(greens, { x = red.x, y = red.y })
	shapeTest[red.y] = shapeTest[red.y] or {}
	table.insert(shapeTest[red.y], red.x)
	local dTop, dBot, dLeft, dRight = getAdjacents(red, reds)
	if dTop then
		for i = 1, dTop - 1 do
			table.insert(greens, { x = red.x, y = red.y - i })
			shapeTest[red.y - i] = shapeTest[red.y - i] or {}
			table.insert(shapeTest[red.y - 1], red.x)
		end
	end
	if dBot then
		for i = 1, dBot - 1 do
			table.insert(greens, { x = red.x, y = red.y + i })
			shapeTest[red.y + 1] = shapeTest[red.y + 1] or {}
			table.insert(shapeTest[red.y + 1], red.x)
		end
	end
	if dLeft then
		for i = 1, dLeft - 1 do
			table.insert(greens, { x = red.x - i, y = red.y })
			table.insert(shapeTest[red.y], red.x - i)
		end
	end
	if dRight then
		for i = 1, dRight - 1 do
			table.insert(greens, { x = red.x + i, y = red.y })
			table.insert(shapeTest[red.y], red.x + i)
		end
	end
end

print("init greens")
print(#shapeTest)
print("shep")

local function castray(point, shape)
	local prev = "."
	local current = "."
	local hits = 0
	for x = 1, point.x do
		print(x)
    print(table.unpack(shapeTest[point.y]))
    if not shapeTest[point.y] then
      print('nothing there')
      goto continue
    end
    for _, v in pairs(shapeTest[point.y]) do
      if x < v -1 and x > v + 1 then
        print("to far")
        goto continue
      end
    end

		for _, v in pairs(shape) do
			if v.x == x and v.y == point.y then
				current = "X"
				break
			end
			current = "."
		end
		if prev and prev == "X" and current == "." then
			hits = hits + 1
		end
		prev = current
    ::continue::
	end
	if current == "X" then
		return true
	end
	if hits % 2 == 0 then
		return false
	end
	return true
end

local function isInside(p1, p2, shape)
	print("TEST")
	p1:print()
	p2:print()
	local startX, endX, startY, endY
	startX = p1.x > p2.x and p2.x or p1.x
	endX = p1.x > p2.x and p1.x or p2.x
	startY = p1.y > p2.y and p1.y or p2.y
	endY = p1.y > p2.y and p2.y or p1.y
	for x = startX, endX do
		if not castray({ x = x, y = p1.y }, shape) then
			return false
		end
		if not castray({ x = x, y = p2.y }, shape) then
			return false
		end
	end
	for y = startY, endY do
		if not castray({ x = p1.x, y = y }, shape) then
			return false
		end
		if not castray({ x = p2.x, y = y }, shape) then
			return false
		end
	end
	return true
end

local distances = getAllDist(reds)
table.sort(distances, function(a, b)
	return a.distance > b.distance
end)
print("distance")
print(#distances)

for _, distance in ipairs(distances) do
	local p1, p2 = distance.p1, distance.p2
	if isInside(p1, p2, greens) then
		print("ISIN")
		print("=====")
		p1:print()
		p2:print()
		print("=====")
		local width = math.abs(p1.x - p2.x) + 1
		local height = math.abs(p1.y - p2.y) + 1
		print(distances[1].distance)
		print("Width : " .. width .. ", Height : " .. height)
		print("Area : " .. width * height)
		break
	end
end
