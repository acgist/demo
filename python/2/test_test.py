def fun(name: str):
    print(f"test_fun {name}")
    return 1

def test_fun():
    assert fun("test") == 2

"""
pytest .\2\test_test.py
"""