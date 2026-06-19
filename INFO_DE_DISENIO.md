Estilo visual inspirado en Steam pero más limpio y minimalista:

- Fondo casi negro con tinte azulado, no negro puro
- Superficies en capas: cada nivel de profundidad es un gris-azul levemente más claro
- Un único color de acento frío (azul eléctrico) para todo lo interactivo: botones, bordes focused, items activos, valores destacados
- Sin gradientes, sin sombras, sin efectos de brillo
- Densidad de información alta pero con respiración: padding generoso, line-height amplio
- Jerarquía visual solo con peso tipográfico y color, no con tamaños extremos
- El estado activo/seleccionado se marca con una barra vertical de 3dp en el acento, no con backgrounds muy diferentes
- Chips de filtro con borde y fondo tintado en el color acento cuando están activos
- Botones primarios en el color acento sólido, botones secundarios como ghost (borde + fondo transparente)
- Íconos outline, nunca filled
- Textos de error y acciones destructivas en rojo desaturado, no rojo brillante

Usá esta paleta y estilo visual para todas las screens:

Colores:
- BgBase    #0E1117  (fondo principal)
- BgSurface #161B27  (cards, paneles)
- Elevated  #1E2537  (hover, items seleccionados)
- Accent    #4D9FFF  (azul eléctrico, elemento interactivo principal)
- AccentDim #1A3A5C  (fondo de badges y chips activos)
- TextPrim  #E8EAF0  (texto principal)
- TextMuted #7A8599  (metadata, subtítulos, placeholders)
- Border    #242D42  (bordes y separadores)
- Danger    #E2574C  (errores, eliminar, favorito activo)

Tipografía:
- Font: Inter (o la sans-serif por defecto del sistema)
- Pesos: 400 regular, 500 medium — nunca 600 ni 700
- Tamaños: 28sp hero, 20sp títulos de screen, 18sp títulos de card, 14sp cuerpo, 13sp secundario, 12sp metadata, 11sp labels en mayúsculas

Forma:
- Border radius: 8dp para la mayoría, 6dp para iconos pequeños, 20dp para chips
- Bordes: 1dp solid Border en cards y campos
- Barra de acento: 3dp vertical en Accent sobre el lado izquierdo del item activo en listas (sin border radius)
- Sin sombras, sin gradientes

Espaciado:
- Padding de pantalla: 24dp
- Gap entre secciones: 20dp
- Gap entre items de lista: 4dp
- Gap entre elementos internos de un item: 14dp