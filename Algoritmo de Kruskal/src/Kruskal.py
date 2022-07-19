class Conjunto:
    def __init__(self, len):
        self.padre = []
        self.rank = []

        #Se crea unconjunto ajeno por vértice (Por eso es importante que los vertices inicien en 0)
        for vertice in range(len):
            self.padre.append(vertice)
            self.rank.append(0)
    #Encuentra el conjunto ajeno donde pertenece el vértice V        
    def encuentra(self, v):
        if self.padre[v] == v:
            return v
        else:
            return self.encuentra(self.padre[v])
    #Une dos conjuntos ajenos        
    def union(self, u, v):
        u_root = self.encuentra(u)
        v_root = self.encuentra(v)
        if self.rank[u_root] < self.rank[v_root]:
            self.padre[u_root] = v_root
        elif self.rank[u_root] > self.rank[v_root]:
            self.padre[v_root] = u_root
        else:
            self.padre[v_root] = u_root
            self.rank[u_root] += 1


def kruskal(vertices,aristas):

    #Ordenamos aristas
    aristas.sort(key = lambda x: x[2])
    #Creamos el conjunto ageno vacío
    conjunto = Conjunto(len(vertices))
    solucion = []
    for (u, v, peso) in aristas:

        #Si no son el mismo pertenecen al mismo conjunto ajeno
        if conjunto.encuentra(u) != conjunto.encuentra(v):
            #Se unen
            solucion.append((u, v, peso))
            conjunto.union(u, v)
    return solucion

#Leemos el archivo de texto y lo convertimos a listas
archivo = open('Grafica.txt', 'r')
Lines = archivo.read().splitlines()  
grafica = []
for line in Lines:
    x = line.split(",")
    y = []
    for n in x:
        y.append(int(n))
    grafica.append(y)       

#Separamos los vertices de las aritas
vertices = grafica.pop(0)
aristas = grafica

#Llamamos kruskal
solucion = kruskal(vertices,aristas)

for vertice in solucion:
    print(vertice)
