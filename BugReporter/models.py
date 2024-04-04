from django.db import models

# Create your models here.

class Employee(models.Model):
    Username = models.CharField(max_length=255, unique=True, null=False)
    Email = models.CharField(max_length=255, unique=True, null=False)
    Role = models.CharField(max_length=100, blank=True)

class User(models.Model):
    Employee = models.ForeignKey(Employee, on_delete=models.CASCADE, null=True, blank=True)
    Username = models.CharField(max_length=255, unique=True, null=False)
    Email = models.CharField(max_length=255, unique=True, null=False)

class Project(models.Model):
    Name = models.CharField(max_length=255, null=False)
    Description = models.TextField(blank=True)
    CreationDate = models.DateField(null=True, blank=True)
    LastAnalysisDate = models.DateField(null=True, blank=True)

class BugReport(models.Model):
    Project = models.ForeignKey(Project, on_delete=models.CASCADE)
    ReportedBy = models.ForeignKey(User, on_delete=models.CASCADE, related_name='reported_bugs')
    AssignedTo = models.ForeignKey(Employee, on_delete=models.CASCADE, related_name='assigned_bugs')
    DateReported = models.DateField()
    Title = models.CharField(max_length=255, blank=True)
    Description = models.TextField(blank=True)
    StepsToReproduce = models.TextField(blank=True)
    Severity = models.CharField(max_length=50, blank=True)
    Priority = models.CharField(max_length=50, blank=True)
    Status = models.CharField(max_length=50, blank=True)
    Reproducible = models.BooleanField(default=False)
    Resolution = models.TextField(blank=True)
    ResolutionDate = models.DateField(null=True, blank=True)
    Comments = models.TextField(blank=True)

class CommitHistory(models.Model):
    Employee = models.ForeignKey(Employee, on_delete=models.CASCADE)
    Project = models.ForeignKey(Project, on_delete=models.CASCADE)
    CommitMessage = models.TextField(blank=True)
    DateTime = models.DateTimeField()
    AffectedFiles = models.TextField(blank=True)

class SourceCode(models.Model):
    Project = models.ForeignKey(Project, on_delete=models.CASCADE)
    FilePath = models.TextField(null=False)
    Checksum = models.CharField(max_length=255, blank=True)

class Notification(models.Model):
    Recipient = models.ForeignKey(User, on_delete=models.CASCADE)
    Type = models.CharField(max_length=100, blank=True)
    Message = models.TextField(blank=True)
    DateTime = models.DateTimeField()
