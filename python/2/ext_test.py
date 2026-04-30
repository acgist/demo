class A:
    def __init__(self):
        print("init A")
        super().__init__()
    def show(self):
        print("show A")
class B:
    def __init__(self):
        print("init B")
        super().__init__()
    def show(self):
        print("show B")
class C(A, B):
    def __init__(self):
        print("init C")
        super().__init__()
        # A.__init__(self)
        # B.__init__(self)
    def show(self):
        # print("show C")
        # super().show()
        A.show(self)
        B.show(self)

print(C.mro())
c = C()
c.show()
