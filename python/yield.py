

def test_yield():
    for i in [1,2,3,5]:
        yield i*1


g=test_yield()

for i in g:
    print(i)



 
