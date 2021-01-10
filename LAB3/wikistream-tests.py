import unittest
import runpy
import os.path
import time

class TestWikistream(unittest.TestCase):

  def test_streaming(self):
      os.system("rm -rf ./input");
      os.mkdir("input");
      os.system('python3 ./wikistream.py ./ &')
      time.sleep(10)
      dirExists = len( os.listdir("./input/") ) == 1
      #self.assertEqual('foo'.upper(), 'FOO')
      self.assertTrue(dirExists)
      #      self.assertFalse('Foo'.isupper())
      #      with self.assertRaises(TypeError):

if __name__ == '__main__':
    unittest.main()
