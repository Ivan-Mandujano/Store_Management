from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
import os
app = Flask(__name__)
#Se obtiene la direccion de la base de datos dentro de nuestra computadora
base_dir = os.path.abspath(os.path.dirname(__file__))
db_path = os.path.join(base_dir, 'database/StoreInventory.db')
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + db_path
db = SQLAlchemy(app)
# Tabla de usuarios

class Users(db.Model):
    __tablename__ = "users"

    user_id = db.Column(db.Integer, primary_key=True)
    first_name = db.Column(db.Text, nullable=False)
    last_name = db.Column(db.Text, nullable=False)
    email = db.Column(db.Text, nullable=False, unique=True)
    password = db.Column(db.Text, nullable=False)
    access_level = db.Column(db.Integer, nullable=False)
# Tabla de tickets de cada compra

class Tickets(db.Model):
    __tablename__ = "tickets"

    ticket_id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey("users.user_id"))
    prod_id = db.Column(db.Integer, db.ForeignKey("productos.prod_id"))
    date = db.Column(db.DateTime)
# Tabla de productos

class Productos(db.Model):
    __tablename__ = "productos"

    prod_id = db.Column(db.Integer, primary_key=True)
    quantity = db.Column(db.Integer)
    prod_name = db.Column(db.Text, nullable=False)
    prod_category = db.Column(db.Text)
    prod_description = db.Column(db.Text)
    prod_quantity = db.Column(db.Integer)
    prod_modified_date = db.Column(db.DateTime)
    prod_price = db.Column(db.Numeric)

    def to_dict(self):
        return {
            "prod_id": self.prod_id,
            "quantity": self.quantity,
            "prod_name": self.prod_name,
            "prod_category": self.prod_category,
            "prod_description": self.prod_description,
            "prod_quantity": self.prod_quantity,
            "prod_modified_date": self.prod_modified_date,
            "prod_price": self.prod_price,
        }
# Tabla de imágenes de productos

class Prod_Images(db.Model):
    __tablename__ = "prod_images"

    photo_id = db.Column(db.Integer, primary_key=True)
    prod_id = db.Column(db.Integer, db.ForeignKey("productos.prod_id"))
    prod_image = db.Column(db.BLOB)

#Aqui registramos al usuario en la base de datos:
@app.route("/register", methods=["POST"])
def register():
    try:
        first_name = request.form.get("first_name")
        last_name = request.form.get("last_name")
        email = request.form.get("email")
        password = request.form.get("password")
        access_level = request.form.get("access_level")

        user = Users(
            first_name=first_name,
            last_name=last_name,
            email=email,
            password=password,
            access_level=access_level,
        )

        db.session.add(user)
        db.session.commit()
        return jsonify({"status": "success", "message": "Usuario registrado exitosamente"})
    except Exception as e:
        return jsonify({'status': 'error', 'message': f'Error al registrar el usuario'})
@app.route("/login", methods=["GET", "POST"])
def login():
    try:
        email = request.form.get("email")
        password = request.form.get("password")

        # Buscar al usuario en la base de datos
        user = Users.query.filter_by(email=email).first()

        if user is not None and user.password == password:
            return jsonify({"status": "success", "message": "Inicio de sesión exitoso"})
        else:
            return jsonify({"status": "error", "message": "Credenciales incorrectas"})
    except Exception as e:
        return jsonify({'status': 'error', 'message': f'Sucedio un error'})

@app.route("/product/new", methods=["POST"])
def new_product():
    try:
        prod_name = request.form.get("prod_name")
        prod_category = request.form.get("prod_category")
        prod_description = request.form.get("prod_description")
        prod_price = float(request.form.get("prod_price"))
        prod_quantity = int(request.form.get("prod_quantity"))

        producto = Productos(
            prod_name=prod_name,
            prod_category=prod_category,
            prod_description=prod_description,
            prod_price=prod_price,
            prod_quantity=prod_quantity,
        )

        db.session.add(producto)
        db.session.commit()

        return jsonify({'status': 'success', 'message': 'Producto creado exitosamente'})
    except Exception as e:
        return jsonify({'status': 'error', 'message': f'Error al crear el producto: {str(e)}'})

# Ruta para ver un producto por ID
@app.route("/product/<prod_id>", methods=["GET"])
def get_product(prod_id):
    producto = Productos.query.filter_by(prod_id=prod_id).first()

    if producto is None:
        return jsonify({'status': 'error', 'message': f'Error al obtener el dato'})
    return jsonify(producto.to_dict())

@app.route("/prod_images/new", methods=["POST"])
def new_prod_image():
    try:
        prod_id = request.form.get("prod_id")
        image_name = request.form.get("image_name")
        image_data = request.files["image_data"].read()

        # Crear el objeto Prod_Images
        prod_image = Prod_Images(
            prod_id=prod_id,
            image_name=image_name,
            image_data=image_data,
        )

        db.session.add(prod_image)
        db.session.commit()
        return jsonify({'status': 'success', 'message': 'Foto ingresada correctamente'})
    except Exception as e:
        return jsonify({'status': 'error', 'message': f'Error al ingresar foto: {str(e)}'})

@app.route("/prod_images/<int:prod_id>", methods=["GET"])
def get_prod_image(prod_id):
    # Obtener la imagen de la base de datos
    prod_image = Prod_Images.query.filter_by(prod_id=prod_id).first()

    # Devolver la imagen
    if prod_image is not None:
        return prod_image.image_data
    else:
        return jsonify({'status': 'error', 'message': f'Error al obtener el dato'})


if __name__ == "__main__":
    app.run(debug=True)

def home():
    return 'hello world'

if __name__ == '__main__':
    # Crea las tablas
    with app.app_context():
        db.create_all()

    # Inicia la aplicación en modo debug
    app.run(debug=True)