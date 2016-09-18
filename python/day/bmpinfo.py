import struct


def is_bmp(filename):

    with open(filename,'rb') as fs:
        _bytes=fs.read(30)
        format_result=struct.unpack('<ccIIIIIIHH',_bytes)
        

is_bmp('images.jpeg')
