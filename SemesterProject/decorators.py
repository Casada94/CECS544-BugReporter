from typing import Callable
from django.shortcuts import redirect

def require_login(func: Callable) -> Callable:
    def wrapper(*args, **kwargs):
        if not args[0].user.is_authenticated:
            return redirect('/account/login')
        return func(*args, **kwargs)
    return wrapper

