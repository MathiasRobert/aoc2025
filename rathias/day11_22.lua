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

local devices = {}
for line in io.lines(path) do
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

local memoize = {}
setmetatable(memoize, { __mode = "v" }) -- make values weak

local function parseDevices(device, dac, fft)
	local key = device .. (dac and "1" or "0") .. (fft and "1" or "0")
	if memoize[key] then
		return memoize[key]
	end

	local count = 0
	for _, entry in pairs(devices[device]) do
		if entry ~= "out" then
			count = count + parseDevices(entry, dac or entry == "dac", fft or entry == "fft")
		elseif dac and fft then
			count = count + 1
		end
	end
	memoize[key] = count
	return count
end

local nb = parseDevices("svr", false, false)
print(nb)
