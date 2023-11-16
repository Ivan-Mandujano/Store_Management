from flask import Flask
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
    email = db.Column(db.Text, nullable=False)
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

# Tabla de imágenes de productos

class Prod_Images(db.Model):
    __tablename__ = "prod_images"

    photo_id = db.Column(db.Integer, primary_key=True)
    prod_id = db.Column(db.Integer, db.ForeignKey("productos.prod_id"))
    prod_image = db.Column(db.BLOB)


@app.route('/')

def home():
    return 'hello world'

if __name__ == '__main__':
    # Crea las tablas
    with app.app_context():
        db.create_all()

    # Inicia la aplicación en modo debug
    app.run(debug=True)