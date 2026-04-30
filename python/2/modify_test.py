def printarr(arr):
    # for item in arr:
    #     print(item)
    print(arr)

def modify(arr):
    arr.append(4)

arr = [1, 2, 3]
printarr(arr)
modify(arr)
printarr(arr)
modify(arr[:])
printarr(arr)