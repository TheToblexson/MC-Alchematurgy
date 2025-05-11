def format(numIn):
    number = float(numIn)
    if number.is_integer():
        return "{:0.1f}".format(number)
    return "{:0.4f}".format(number)

with open("Essences - 1.21.0.csv", "rt") as csv:
    string = csv.read()
array = string.split(",")
selection = ""
recipes = []
i = 0
iterations =  100

#turn read string into a usable list
for word in array:
    #if i > iterations:
    #    break
    word = word.strip()
    if word == "util" or word == "minecraft":
        recipe = selection.split(",")
        recipes.append(recipe)
        selection = ""
        i += 1
    if word != "":
        selection += word + ","
recipe = selection.split(",")
recipes.append(recipe)
        
#extract essence order from header
recipes.pop(0)
        
#remove util recipes
newRecipes = []
for recipe in recipes:
    if recipe[0] != "util":
        newRecipes.append(recipe)
recipes = newRecipes

#remove essence ingredients (used in sheets for calculation)
newRecipes = []
newRecipe = []
for recipe in recipes:
    try:
        newRecipe = []
        for i in range(9):
            newRecipe.append(recipe[i])
        newRecipes.append(newRecipe)
    except:
        print(f"Error: entry is missing data...{recipe}")
recipes = newRecipes

#format into JSON

#open file
output = "{\n"
#open values
output += "  \"values\": {\n"
for recipe in recipes:
    type = recipe.pop(0)
    name = recipe.pop(0)
    valid = recipe.pop(0)
    if valid == "Yes":
        air = recipe.pop(0)
        earth = recipe.pop(0)
        fire = recipe.pop(0)
        water = recipe.pop(0)
        life = recipe.pop(0)
        magic = recipe.pop(0)
        #open recipe
        output += f"    \"{type}:{name}\": {{\n"
        #essences
        output += f"      \"air\": {format(air)},\n"
        output += f"      \"earth\": {format(earth)},\n"
        output += f"      \"fire\": {format(fire)},\n"
        output += f"      \"water\": {format(water)},\n"
        output += f"      \"life\": {format(life)},\n"
        output += f"      \"magic\": {format(magic)}\n"
        #close recipe
        output += "    },\n"
#remove last comma
output = output[:-2] + "\n"
#close values
output += "  }\n"
#close file
output += "}"

#create JSON
with open("essences.json", "w") as json:
        json.write(output)

#print(output)
#print("\n"*20)



