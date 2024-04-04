from django.contrib.auth.decorators import login_required
from django.shortcuts import render, redirect

# Create your views here.
from django.shortcuts import render
from django.http import HttpResponse
from django.template import loader
from SemesterProject.decorators import require_login


@require_login
def home(request):
    template = loader.get_template('home.html')
    context = {
        'users': request.user,
    }
    return HttpResponse(template.render(context,request))


