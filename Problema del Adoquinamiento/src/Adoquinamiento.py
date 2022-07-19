import tkinter as tk
from tkinter import simpledialog
from tkinter import messagebox
import numpy as np
import random
import time 
class tablero(tk.Frame):
    def __init__(self, padre, tamaño, tcuadro=25):           
        self.tabla = tabla;        
        self.renglones = tamaño
        self.columnas = tamaño
        self.tcuadro = tcuadro        
        canvas_width = tamaño * tcuadro
        canvas_height = tamaño * tcuadro
        tk.Frame.__init__(self, padre)
        self.canvas = tk.Canvas(self, borderwidth=0, highlightthickness=0,
                                width=canvas_width, height=canvas_height, background="white")
        self.canvas.pack(side="top", fill="both", expand=True, padx=2, pady=2)
        self.pinta()

    def pinta(self):                
        tcuadro = int(950 / self.columnas)
        tcuadro = int(950 / self.renglones)
        self.tcuadro = min( tcuadro,  tcuadro)
        r = random.randint(2,1670)           
        for row in range(self.renglones):            
            for col in range(self.columnas):
                x1 = (col * self.tcuadro)
                y1 = (row * self.tcuadro)
                x2 = x1 + self.tcuadro
                y2 = y1 + self.tcuadro
                d = self.tabla.item(row,col)
                if(self.tabla.item(row,col) != 0):
                    self.canvas.create_rectangle(x1, y1, x2, y2, outline="black", fill="#"+("%06x"%(r*d)), tags="square")                    
                else:         
                    self.canvas.create_rectangle(x1, y1, x2, y2, outline="black", fill="white", tags="square")  

    def split(array, nrenglones, ncols):   
        r, h = array.shape
        return (array.reshape(h//nrenglones, nrenglones, -1, ncols)
                     .swapaxes(1, 2)
                     .reshape(-1, nrenglones, ncols))

    def resuelve(self,matrix):
        print("--------------------------")                 
        coord = 0
        disc = 0
        if len(matrix) == 0:
            return []      
        if len(matrix) == 2:
            print("------CASO BASE---------")
            print(matrix)
            x = random.randint(2,100)  
            if(matrix.item(0,0) != 0):
                matrix.itemset((0,1),x)
                matrix.itemset((1,0),x) 
                matrix.itemset((1,1),x)
            elif(matrix.item(0,1) != 0):
                matrix.itemset((0,0),x)
                matrix.itemset((1,0),x) 
                matrix.itemset((1,1),x)
            elif(matrix.item(1,0) != 0):
                matrix.itemset((0,0),x)
                matrix.itemset((0,1),x) 
                matrix.itemset((1,1),x)
            elif(matrix.item(1,1) != 0):
                matrix.itemset((0,1),x)
                matrix.itemset((1,0),x) 
                matrix.itemset((0,0),x)                
            self.pinta()
            print("------RESULTADO---------")
            print(matrix)
                                   
        else:
            print("------CASO RECURSIVO---------")
            print(matrix)
            for i in range (len(matrix)):   
                for j in range(len(matrix)):
                    if(matrix.item(j,i) != 0):
                        disc = matrix.item(j,i)
                        if(i < int(len(matrix))/2 and j < int(len(matrix))/2 ):
                            coord = 1
                            break
                        elif(i >= int(len(matrix))/2 and j < int(len(matrix))/2 ):
                            coord = 2
                            break
                        elif(i < int(len(matrix))/2 and j >= int(len(matrix))/2 ):
                            coord = 3
                            break
                        elif(i >= int(len(matrix))/2 and j >= int(len(matrix))/2 ):
                            coord = 4
                            break     
            
            n = int(len(matrix)/2)
            x = random.randint(2,100)
            if(coord == 1):
                matrix.itemset((n,n-1),x)
                matrix.itemset((n-1,n),x)
                matrix.itemset((n,n),x)                
            elif(coord == 2):
                matrix.itemset((n-1,n-1),x)
                matrix.itemset((n,n-1),x)
                matrix.itemset((n,n),x)
            elif(coord == 3):
                matrix.itemset((n-1,n-1),x)
                matrix.itemset((n-1,n),x)
                matrix.itemset((n,n),x)
            elif(coord == 4):
                matrix.itemset((n-1,n-1),x)
                matrix.itemset((n-1,n),x)
                matrix.itemset((n,n-1),x)
            mitad1 = np.hsplit(np.vsplit(matrix, 2)[0], 2)
            mitad2 = np.hsplit(np.vsplit(matrix, 2)[1], 2)            
            a = mitad1[0]
            b = mitad1[1]
            c = mitad2[0]
            d = mitad2[1]
            print("")
            print(matrix)             
            self.resuelve(a)
            self.resuelve(b)
            self.resuelve(c)
            self.resuelve(d)
            self.pinta()
            resultado = np.vstack([np.hstack([a,b]), np.hstack([c,d])])
            print("------- Resultado de caso recursivo------------")
            print(resultado)            
            return(resultado)            


                



if __name__ == "__main__":
    root = tk.Tk()
    root.title("Adoquinamiento")
    root.minsize(width=950, height=950)
    root.maxsize(width=950, height=950)
    root.geometry('950x950+450+30')
    tamaño = simpledialog.askinteger("Entrada", "Ingresa una potenica de 2 por favor",
                                 parent=root,
                                 minvalue=0)


    if (tamaño & (tamaño-1) == 0) and tamaño != 0:
        tabla = np.zeros((tamaño,tamaño), dtype = int);
        x=random.randint(0, tamaño-1) 
        y=random.randint(0, tamaño-1)
        tabla.itemset((y,x),1) 
        print("-----TABLA INICIAL------")
        print(tabla)
        tablero = tablero(root,tamaño)
        tablero.pack(side="top", fill="both", expand="true", padx=4, pady=4)
        root.deiconify()
        tablero.resuelve(tabla)
        print("-------- TABLA FINAL ----------")
        print(tabla)    
        root.mainloop()
    else:
        messagebox.showinfo(message="Ese número no es potencia de 2", title="Error")
