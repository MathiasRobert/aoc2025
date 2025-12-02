import re

with open("../inputs/day2_1.txt", "r") as f:
    data = f.read()

ranges = data.split(",")
invalidIds = 0
invalidList = []
for rng in ranges:
	parts = rng.split("-")
	first = parts[0]
	last = parts[1]
	for i in range(int(first), int(last) + 1):
		txt = str(i)
		x = re.search(r"^(\d+)\1+$", txt)
		if x:
			invalidIds += i
			invalidList.append(i)
print(invalidIds)