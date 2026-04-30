sorts = []
source = r"D:/tmp/sort.txt"
target = source + ".txt"
with open(source, "r", encoding = "utf-8") as input:
    with open(target, "w", encoding = "utf-8") as output:
        lines = input.readlines()
        for line in lines:
            line = line.rstrip() + "\n"
            if line == "\n":
                sorts.sort()
                output.writelines(sorts)
                output.write("\n")
                sorts = []
            else:
                sorts.append(line)
if len(sorts) > 0:
    with open(target, "a", encoding = "utf-8") as output:
        sorts.sort()
        output.writelines(sorts)
        # output.write("\n")
        sorts = []
with open(source, "r", encoding = "utf-8") as input:
    with open(target, "r", encoding = "utf-8") as output:
        lines_1 = set(map(str.rstrip, input.readlines()))
        lines_2 = set(map(str.rstrip, output.readlines()))
        lines_3 = lines_1.difference(lines_2)
        print(lines_3)
        
