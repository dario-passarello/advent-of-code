class Tree:
    
    def __init__(self,name):
        self.name = name
        self.children = []
        self.level = 0
        self.parent = None
    def add_children(self,other):
        self.children.append(other)
        other.parent = self
    def set_level(self,i):
        self.level =i

def build_tree(filename):
    pFile = open(filename,'r')
    nodes = {}
    for linee in pFile:
        line = linee.strip('\n')
        nodes[line.split(')')[1]] = Tree(line.split(')')[1])
    nodes["COM"] = Tree("COM")
    pFile.close()
    pFile = open(filename,'r')
    for linee in pFile:
        line = linee.strip('\n')
        nodes[line.split(')')[0]].add_children(nodes[line.split(')')[1]])
    return nodes

def calc_level(node, lvl):
    node.set_level(lvl) 
    lvl_sum = 0
    for item in node.children:
        lvl_sum += calc_level(item, lvl + 1)
    return lvl_sum + lvl 

def find_path_to_head(node):
    a_node = node
    path = []
    while a_node != None:
        path.insert(0,a_node.name)
        a_node = a_node.parent
    return path
def find_common_node(node1, node2):
    path1 = find_path_to_head(node1)
    path2 = find_path_to_head(node2)
    i = 0
    while path1[i] == path2[i]:
        i += 1
    return len(path1) - i - 2 + len(path2) - i
nodes = build_tree("6.in")

print("FIRST HALF: " + str(calc_level(nodes["COM"],0)))
print("SECON HALF: " + str(find_common_node(nodes["YOU"],nodes["SAN"])))
