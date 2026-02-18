{{- define "my-deployment.tpl" -}}
apiVersion: apps/v1
kind: Deployment
{{- with .Values.appName }}
metadata:
  name: {{ .app }}-dpl
spec:
  replicas: {{ $.Values.replicas }}
  selector:
    matchLabels:
      app: {{ .app }}
  template:
    metadata:
      labels:
        app: {{ .app }}
    spec:
      nodeSelector:
      {{- toYaml $.Values.nodeSelector | nindent 7 }}
      containers:
      - name: {{ .app }}-cont
      {{- with $.Values.container}}
        image: {{ .image }}
        imagePullPolicy: {{ .pullPolicy }}
        ports:
        - containerPort: {{ .port }}
        {{- if $.Values.volumeMounts }}
        volumeMounts:
        {{- range  $.Values.volumeMounts }}
          - name: {{.name}}
            mountPath: {{.mountPath}} 
        {{- end }}
        {{- end}}
        {{- if $.Values.container.envs}}
        env:
        {{- range .envs }}
          - name: {{ .name }}
            value: {{ .value }}
        {{- end}}
        {{- end}}
      {{- end}}

      {{- if $.Values.volumes }}
      volumes:
      {{- toYaml $.Values.volumes | nindent 8 }}
      {{- end }}
{{- end}}
{{- end}}