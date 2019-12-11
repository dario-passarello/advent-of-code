import math

EPSILON = 0.00000000001

#Reads the problem input from file
def scan_map(filename):
    pFile = open(filename,'r')
    x = 0 
    y = 0
    points = {}
    for line in pFile:
        x = 0
        for char in line:
            if char == '#':
                points[(x,y)] = 1
            x += 1
        y += 1 
    return points

#Return the minimal ("with lowest components") integer vector with the same direction of the line from point1 and point2
def get_search_vector(point1,point2):
    vector = (point2[0] - point1[0], point2[1] - point1[1]) #Get the vector from point1 to point2
    gcd = math.gcd(vector[0],vector[1])                     #Find the g.c.d. of the vector components
    minimal_vector = (int(vector[0]/gcd),int(vector[1]/gcd))#Simplify the vector
    return minimal_vector

#Returns true if vis_point is visible from start point (e.g. there aren't other points between the two points)
def is_visible(points,start_point, vis_point):
    s_vector = get_search_vector(start_point, vis_point)
    s_point = (start_point[0] + s_vector[0], start_point[1] + s_vector[1])
    obstruction = False
    while s_point != vis_point: #Follow s_vector until you reach vis_point
        if s_point in points:
            obstruction = True
        s_point = (s_point[0] + s_vector[0], s_point[1] + s_vector[1])
    return not obstruction

#Counts the number of points visible from start_point
def count_visibility(start_point, points):
    count = 0
    for key in points:
        if key != start_point:
            if is_visible(points,start_point,key):
                count += 1
    return count

#find the point with the maximum visibility 
def max_visibility(points):
    max_count = -1
    max_point = None 
    for key in points:
        count = count_visibility(key, points)
        if count > max_count:
            max_count = count
            max_point = key
    return (max_count,max_point)

#measure the distance of two angles (between -pi and pi)
def angle_distance(angle1,angle2):
    if angle1 <= angle2:
        return angle2 - angle1
    else:
        return angle_distance(angle1,math.pi) + angle_distance(-math.pi,angle2)

#find the point with minimum angle distrance from angle (the distance must not be 0)
def find_next_point(points,start_point,angle):
    min_angle_dist = None
    next_min_angle = None
    min_point = None
    for key in points:
        if start_point != key and is_visible(points,start_point,key):
            s_vector = get_search_vector(start_point, key)
            key_angle = math.atan2(s_vector[1], s_vector[0])
            a_distance = angle_distance(angle, key_angle)
            if min_angle_dist == None or (a_distance < min_angle_dist and a_distance > EPSILON):
                next_min_angle = key_angle
                min_angle_dist = a_distance
                min_point = key
    return min_point

#find the 200th annihilated point
def find_200(points,start_point):
    last_point = None
    starting_angle = -math.pi/2 - EPSILON
    destroy_point = None
    for i in range(200):
        destroy_point = find_next_point(points,start_point,starting_angle)
        del(points[destroy_point])
        vector_to_next = get_search_vector(start_point,destroy_point)
        starting_angle = math.atan2(vector_to_next[1],vector_to_next[0])
        print(str(i) + ":" + str(destroy_point) )
    return destroy_point

def main():
    points = scan_map("10.in")
    base_point = max_visibility(points)
    print("FIRST STATEMENT:" + str(base_point)) 
    print("SECOND STATEMENT: " + str(find_200(points,base_point[1]))) 

main()
