#!/bin/bash
for i in $(seq 1 20); do
  NUMENTIDADES=$((RANDOM % 5 + 2))
  CANTIDADES='['
  NOMBRES='['
  for j in $(seq 1 $NUMENTIDADES); do
    CANTIDAD=$((RANDOM % 15 + 5))
    CANTIDADES+="$CANTIDAD"
    NOMBRES+="\"Entidad${j}\""
    if [ "$j" -lt "$NUMENTIDADES" ]; then
      CANTIDADES+=","
      NOMBRES+=","
    fi
  done
  CANTIDADES+=']'
  NOMBRES+=']'
  BODY="{\"cantidadesIniciales\":$CANTIDADES,\"nombreEntidades\":$NOMBRES}"
  echo "Enviando $NUMENTIDADES entidades: $BODY"
  curl -s -X POST "http://localhost:8080/Solicitud/Solicitar?nombreUsuario=sadie27" \
    -H "Content-Type: application/json" -d "$BODY" &
done
wait