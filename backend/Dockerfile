# Use an official Python runtime as a base image
FROM python:3.11-slim

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

# Set the working directory in the container
WORKDIR /app

# Copy only requirements first (if you have one), else skip this
COPY requirements.txt /app/
RUN pip install --no-cache-dir -r requirements.txt

# Copy the necessary files and folders
COPY config.py run.py /app/
COPY app/ /app/app/
COPY data/ /app/data/

# Expose the port your Flask app runs on (default 5000)
EXPOSE 5000

# Run the Flask app
CMD ["python", "run.py"]
