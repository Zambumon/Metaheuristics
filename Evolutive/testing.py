import os

if __name__ == '__main__':
    for x in range(0, 10):
        os.system('java -jar build/libs/Evolutive-1.0.jar > test'+ str(x) +'.txt')