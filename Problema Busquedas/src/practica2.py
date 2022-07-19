import random 

def encuentra(a, z):        
    cota_sup = len(a)-1
    cota_inf = 0   
    j = 0 
    while cota_inf <= cota_sup:       
        j = int((cota_inf+cota_sup)/2)

        print (cota_inf,cota_sup,j)
        if(a[j]==z):
            return j
        elif a[j]< z:
            cota_inf = j+1
        else:
            cota_sup = j-1
    return -1


def main():    
    n = int(input ("cantidad de números en el arreglo: ")) #Para usar una entrada dada por el usuario y comentar la linea de abajo
    #n = random.randint(0, 20) #Quitar comentario para hacer mejores pruebas
    a = []
    r = random.randint(0, 200) #Rango de los números del arreglo
    a.append(r)

    for i in range(n-1):
        r = random.randint(0, 1) #Se puede cambiar el 0 por -1 pero puede pasar que el arreglo generado no cumpla que A[0] < A[n-1]              
        a.append(a[len(a)-1]+r)
    print(a)
    z = random.randint(a[0],a[len(a)-1])
    print("Se va a buscar en número ",z, " En el arreglo")    
    print("Se ha encontrado el número: ",z," en la posición: ", encuentra(a,z))
     

main()