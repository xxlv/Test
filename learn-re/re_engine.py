"""
    正则表达式引擎实现
    1 读取pattern
    2 遍历source 尝试

"""

class Re:


    @staticmethod
    def test(pattern,source):
        """
            返回一个boolean值，检查source是否满足pattern
        """
        if not Re.__check_pattern_is_ok__(pattern):
            raise "Pattern is wrong"
        # \w+ 匹配任意字符至少一个
        
        match=[source]
        for s in source:

        return True


    @staticmethod
    def __check_pattern_is_ok__(pattern):
        """
            Check pattern is valid
        """

        start=pattern[0]
        end=pattern[-1]

        if start!=end:
            return False
        else:
            return start=="/"



Re.test("/HELLO/","HELLO")
