import unittest
from mydict import Dict
class TestDict(unittest.TestCase):

    def setUp(self):
        print("set up")

    def tearDown(self):
        print("down")

    def test_init(self):
        d=Dict(a=1,b='test')
        self.assertEqual(d.a,1)
        self.assertEqual(d.b,'test')
        self.assertTrue(isinstance(d,dict))





if __name__=='__main__':
    unittest.main()
